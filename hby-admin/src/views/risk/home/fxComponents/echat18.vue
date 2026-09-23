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
    data() {
      return {
        companyNames: [],
        completedData: [],
        uncompletedData: [],
        overdueData: [],
        orgIds: [], // 存储组织ID
      }
    },
    mounted() {
      this.fetchData()
    },
    methods: {
      async fetchData() {
        try {
          const res = await getControlmatrixCount()
          if (res && res.code === 1 && res.data) {
            this.processChartData(res.data)
            this.initChart()
          }
        } catch (error) {
          console.error('获取数据失败:', error)
        }
      },
      processChartData(data) {
        // 根据接口返回的实际数据结构进行处理
        // data格式: { orgnames: [...公司名称], orgids: [...组织ID], results: { wc: [...已完成], wwc: [...未完成], yq: [...逾期] } }
        this.companyNames = data.orgnames || []
        this.orgIds = data.orgids || []
        this.completedData = (data.results?.wc || []).map((item) =>
          Number(item)
        )
        this.uncompletedData = (data.results?.wwc || []).map((item) =>
          Number(item)
        )
        this.overdueData = (data.results?.yq || []).map((item) => Number(item))
      },
      initChart() {
        const chartDom = this.$refs.chart
        const myChart = echarts.init(chartDom)

        const option = {
          title: {
            text: '公司月度评估管控措施完成情况',
            left: 'left',
            textStyle: { fontSize: 14, color: '#FF8C00' },
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow',
            },
          },
          legend: {
            data: ['已完成', '未完成', '逾期'],
            left: 'right',
          },
          xAxis: {
            type: 'category',
            data: this.companyNames,
            axisLabel: {
              fontSize: 10,
              formatter: function (value) {
                // 每六个字换一行
                return value
                  .split(/(.{6})/)
                  .filter((s) => s)
                  .join('\n')
              },
            },
          },
          yAxis: {
            type: 'value',
          },
          series: [
            {
              name: '已完成',
              type: 'bar',
              data: this.completedData,
              itemStyle: {
                color: '#67C23A',
              },
              label: {
                show: true,
                position: 'top',
                formatter: '{c}',
              },
            },
            {
              name: '未完成',
              type: 'bar',
              data: this.uncompletedData,
              itemStyle: {
                color: '#F56C6C',
              },
              label: {
                show: true,
                position: 'top',
                formatter: '{c}',
              },
            },
            {
              name: '逾期',
              type: 'bar',
              data: this.overdueData,
              itemStyle: {
                color: '#E6A23C',
              },
              label: {
                show: true,
                position: 'top',
                formatter: '{c}',
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
          const { dataIndex, seriesName } = params
          const orgId = this.orgIds[dataIndex]
          const companyName = this.companyNames[dataIndex]

          // 根据系列名称确定type参数
          let type = ''
          switch (seriesName) {
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
            orgid: orgId,
          })
          this.showControlMeasuresDialog(res.data.data, companyName, seriesName)
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
      window.removeEventListener('resize', () => myChart.resize())
    },
  }
</script>

<style scoped></style>
