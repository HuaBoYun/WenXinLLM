<template>
  <div>
    <div
      ref="chart"
      style="width: 100%; height: 360px; padding: 10px; margin-bottom: 10px"
    ></div>
    <!-- 管控措施弹窗 -->
    <ControlMeasuresList ref="controlMeasuresDialog" />
  </div>
</template>

<script>
  import * as echarts from 'echarts'
  import {
    getControlmatrixCount,
    getControlmatrixList,
  } from '@/api/risk/home.js'
  import ControlMeasuresList from '@/views/risk/treatment/controlMeasuresList.vue'

  export default {
    components: {
      ControlMeasuresList,
    },
    props: {
      orgid: {
        type: [String, Number],
        default: '1000',
      },
    },
    data() {
      return {
        chartData: [], // 初始化为空数组
      }
    },
    mounted() {
      this.fetchData() // 在组件挂载后获取数据
    },
    watch: {
      chartData: {
        handler() {
          this.initChart()
        },
        deep: true,
      },
      orgid: {
        immediate: true, // 立即执行一次
        handler(newVal, oldVal) {
          if (newVal != oldVal) {
            this.fetchData()
          }
        },
      },
    },
    methods: {
      async fetchData() {
        try {
          const { data, code, msg } = await getControlmatrixCount({
            orgid: this.orgid,
          })

          if (code === 1 && data) {
            // 根据实际返回数据结构处理
            // 数据格式为 { wc: 已完成数, wwc: 未完成数, yq: 逾期数 }
            this.chartData = [
              { name: '已完成', value: data.wc ? Number(data.wc) : 0 },
              { name: '未完成', value: data.wwc ? Number(data.wwc) : 0 },
              { name: '逾期', value: data.yq ? Number(data.yq) : 0 },
            ]
          }
        } catch (error) {
          console.error('获取数据失败:', error)
        }
      },
      initChart() {
        const chartDom = this.$refs.chart
        const myChart = echarts.init(chartDom)

        const option = {
          title: {
            text: '月度评估管控措施完成情况',
            left: 'left',
            textStyle: { fontSize: 14, color: '#FF8C00' },
          },
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)',
          },
          legend: {
            orient: 'horizontal',
            bottom: 0,
            left: 'center',
            data: this.chartData.map((item) => item.name),
          },
          series: [
            {
              name: '控制矩阵统计',
              type: 'pie',
              radius: '50%',
              center: ['50%', '43%'],
              data: this.chartData,
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)',
                },
              },
              label: {
                show: true,
                formatter: '{b}: {c} ({d}%)', // 显示名称、数值和百分比
              },
              itemStyle: {
                // 设置不同数据项的颜色
                color: function (params) {
                  const colorList = ['#67C23A', '#F56C6C', '#E6A23C']
                  return colorList[params.dataIndex]
                },
              },
            },
          ],
        }

        // 添加点击事件监听
        myChart.on('click', (params) => {
          console.log('🚀 ~ myChart.on ~ params:', params)
          this.handleChartClick(params)
        })

        myChart.setOption(option)

        // 自适应窗口大小
        window.addEventListener('resize', () => {
          myChart.resize()
        })
      },
      // 处理图表点击事件
      async handleChartClick(params) {
        try {
          const { name } = params

          // 根据饼图扇形名称确定type参数
          let type = ''
          switch (name) {
            case '已完成':
              type = 'wc'
              break
            case '未完成':
              type = 'wwc'
              break
            case '逾期':
              type = 'yq'
              break
            default:
              return
          }

          // 调用接口获取管控措施列表
          const res = await getControlmatrixList({
            type: type,
            orgid: this.orgid,
          })
          this.showControlMeasuresDialog(res.data.data, '当前组织', name)
        } catch (error) {
          console.error('获取管控措施数据失败:', error)
          this.$message.error('获取数据失败')
        }
      },
      // 显示管控措施弹窗
      showControlMeasuresDialog(data, companyName, statusName) {
        // 调用新组件的show方法
        if (
          this.$refs.controlMeasuresDialog &&
          this.$refs.controlMeasuresDialog.show
        ) {
          this.$refs.controlMeasuresDialog.show(data, companyName, statusName)
        } else {
          console.log('管控措施数据:', data)
          this.$message.info(
            `${companyName} - ${statusName}的管控措施数据已在控制台输出`
          )
        }
      },
    },
    beforeDestroy() {
      // 移除事件监听
      window.removeEventListener('resize', () => {
        if (myChart) myChart.resize()
      })
    },
  }
</script>

<style scoped></style>
