<template>
  <div>
    <select v-model="selectedYear" @change="updateChart" class="select">
      <option v-for="year in yearList" :key="year" :value="year">
        {{ year }}
      </option>
    </select>
    <div
      ref="chart"
      style="width: 100%; height: 360px; padding: 10px; margin-bottom: 10px"
    ></div>
    <RiskDatabaseDialog ref="riskDialog" />
    <!-- 催办统计柱状图 -->
    <div
      ref="pieChart"
      style="width: 100%; height: 360px; padding: 10px; margin-bottom: 10px"
    ></div>

    <!-- 催办统计详情弹窗 -->
    <reminder-detail-dialog ref="reminderDialog" />
  </div>
</template>

<script>
  import * as echarts from 'echarts'
  import { getCountByOrg, getList } from '@/api/risk/home.js'
  import ReminderDetailDialog from './ReminderDetailDialog.vue'
  import RiskDatabaseDialog from './RiskDatabaseDialog.vue'

  export default {
    components: {
      ReminderDetailDialog,
      RiskDatabaseDialog,
    },
    data() {
      return {
        riskData: [], // 初始化为空数组
        pieData: {}, // 柱状图数据
        yearList: [], // 年份列表
        selectedYear: null, // 当前选中的年份
        isPlaying: false,
      }
    },
    mounted() {
      // 初始化年份列表
      const currentYear = new Date().getFullYear()
      this.yearList = [currentYear - 2, currentYear - 1, currentYear]
      this.selectedYear = currentYear
      this.fetchRiskData() // 在组件挂载后获取数据
      this.fetchPieData() // 获取柱状图数据
    },
    computed: {
      iconClass() {
        return this.isPlaying ? 'el-icon-video-pause' : 'el-icon-video-play'
      },
    },
    watch: {
      riskData: {
        handler(newVal) {
          if (newVal && newVal.length > 0) {
            // 只有当数据不为空时才初始化图表
            // 注意：这里不需要调用initChart，因为在fetchRiskData中已经处理
          }
        },
        deep: true,
      },
    },
    methods: {
      async fetchRiskData() {
        try {
          const { data, code, msg } = await getCountByOrg({
            year: this.selectedYear,
            type: 2,
          })

          // 处理新的数据格式
          if (data.list && Array.isArray(data.list) && data.list.length > 0) {
            this.riskData = data.list.map((item) => ({
              name: item.NAME,
              value: item.NUMBER,
            }))

            // 按数值从大到小排序
            this.riskData.sort((a, b) => b.value - a.value)
            this.initChart()
          } else if (data.xAxis && data.yAxis) {
            // 保留原有的处理逻辑，以防接口返回格式变化
            const xData = data.xAxis
            const yData = data.yAxis
            const seriesNames = Object.keys(yData)

            this.riskData = xData.map((company, index) => {
              const companyData = { name: company }
              seriesNames.forEach((name) => {
                companyData[name] = yData[name][index]
              })
              return companyData
            })

            if (this.riskData.length > 0) {
              this.initChart()
            } else {
              this.clearChart()
            }
          } else {
            this.riskData = []
            this.clearChart()
          }
        } catch (error) {
          console.error('获取数据失败:', error)
          this.riskData = []
          this.clearChart()
        }
      },

      // 清除图表
      clearChart() {
        const chartDom = this.$refs.chart
        if (chartDom) {
          // 先检查是否已有图表实例
          let myChart = echarts.getInstanceByDom(chartDom)
          if (myChart) {
            myChart.clear()
            myChart.dispose()
          }

          // 创建新的图表实例
          myChart = echarts.init(chartDom)

          // 设置只有标题的空图表
          const emptyOption = {
            title: {
              text: `${this.selectedYear}年全集团风险趋势图`,
              left: 'left',
              textStyle: { fontSize: 14, color: '#FF8C00' },
            },
            xAxis: {
              type: 'category',
              data: [],
            },
            yAxis: {
              type: 'value',
            },
            series: [],
          }
          myChart.setOption(emptyOption)
        }
      },

      initChart() {
        const chartDom = this.$refs.chart
        if (!chartDom) return

        // 清除之前的图表实例
        this.clearChart()

        const myChart = echarts.init(chartDom)

        // 检查数据格式，确定使用哪种图表配置
        const isNewFormat =
          this.riskData.length > 0 && 'value' in this.riskData[0]

        let option

        if (isNewFormat) {
          // 新数据格式的图表配置
          option = {
            title: {
              text: `${this.selectedYear}年全集团风险趋势图`,
              left: 'left',
              textStyle: { fontSize: 14, color: '#FF8C00' },
            },
            tooltip: {
              trigger: 'axis',
              axisPointer: {
                type: 'shadow',
              },
            },
            xAxis: {
              type: 'category',
              data: this.riskData.map((item) => item.name),
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
                type: 'bar',
                data: this.riskData.map((item) => item.value),
                label: {
                  show: true,
                  position: 'top',
                  formatter: '{c}',
                },
              },
            ],
          }
        } else {
          // 原有数据格式的图表配置
          const seriesNames = Object.keys(this.riskData[0] || {}).filter(
            (key) => key !== 'name'
          )

          option = {
            title: {
              text: `${this.selectedYear}年风险领域统计`,
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
              data: seriesNames,
              left: 'right',
            },
            xAxis: {
              type: 'category',
              data: this.riskData.map((item) => item.name),
              axisLabel: {
                fontSize: 10,
                formatter: function (value) {
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
            series: seriesNames.map((name) => ({
              name,
              type: 'bar',
              data: this.riskData.map((item) => item[name]),
              label: {
                show: true,
                position: 'top',
                formatter: '{c}',
              },
            })),
          }
        }

        // 添加点击事件监听
        myChart.on('click', (params) => {
          this.$refs.riskDialog.show({
            unitname: params.name,
            year: this.selectedYear,
          })
        })

        // 设置自适应
        window.addEventListener('resize', () => {
          myChart.resize()
        })

        myChart.setOption(option)
      },

      updateChart() {
        // 年份切换时重新调用接口获取数据
        this.fetchRiskData()
        this.fetchPieData()
      },

      // 获取柱状图数据
      async fetchPieData() {
        try {
          const { data, code, msg } = await getList({
            year: this.selectedYear,
          })

          if (code === 200 && data) {
            // 构建柱状图数据 - 显示一体化管控措施和月度评估两类，每类四种状态
            this.pieData = {
              categories: ['一体化管控措施', '月度评估'],
              // 一体化管控措施的四种数据
              integratedCompleted:
                data.integratedControlMeasuresCompleteCount || 0,
              integratedNoComplete:
                data.integratedControlMeasuresNoCompleteCount || 0,
              integratedOverdue:
                data.integratedControlMeasuresOverdueCount || 0,
              integratedTotal: data.integratedControlMeasuresTotal || 0,
              // 月度评估的四种数据
              monthlyCompleted: data.monthlyCompleteCount || 0,
              monthlyNoComplete: data.monthlyNoCompleteCount || 0,
              monthlyOverdue: data.monthlyOverdueCount || 0,
              monthlyTotal: data.monthlyTotal || 0,
            }

            this.initPieChart()
          } else {
            this.pieData = {}
            this.clearPieChart()
          }
        } catch (error) {
          console.error('获取柱状图数据失败:', error)
          this.pieData = {}
          this.clearPieChart()
        }
      },

      // 初始化柱状图
      initPieChart() {
        const chartDom = this.$refs.pieChart
        if (!chartDom) return

        // 清除之前的图表实例
        let myChart = echarts.getInstanceByDom(chartDom)
        if (myChart) {
          myChart.clear()
          myChart.dispose()
        }

        myChart = echarts.init(chartDom)

        const option = {
          title: {
            text: `催办统计`,
            left: 'left',
            textStyle: { fontSize: 14, color: '#FF8C00' },
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow',
            },
            formatter: function (params) {
              let result = params[0].name + '<br/>'
              params.forEach(function (item) {
                result +=
                  item.marker + item.seriesName + ': ' + item.value + '<br/>'
              })
              return result
            },
          },
          legend: {
            data: ['完成数量', '未完成数量', '超期数量', '总数量'],
            top: 30,
            left: 'center',
          },
          xAxis: {
            type: 'category',
            data: this.pieData.categories || ['一体化管控措施', '月度评估'],
            axisLabel: {
              fontSize: 12,
            },
          },
          yAxis: {
            type: 'value',
            axisLabel: {
              fontSize: 10,
            },
          },
          series: [
            {
              name: '完成数量',
              type: 'bar',
              data: [
                this.pieData.integratedCompleted || 0,
                this.pieData.monthlyCompleted || 0,
              ],
              itemStyle: {
                color: '#5cb87a', // 绿色表示完成
              },
              label: {
                show: true,
                position: 'top',
                formatter: '{c}',
                fontSize: 10,
              },
            },
            {
              name: '未完成数量',
              type: 'bar',
              data: [
                this.pieData.integratedNoComplete || 0,
                this.pieData.monthlyNoComplete || 0,
              ],
              itemStyle: {
                color: '#e6a23c', // 橙色表示未完成
              },
              label: {
                show: true,
                position: 'top',
                formatter: '{c}',
                fontSize: 10,
              },
            },
            {
              name: '超期数量',
              type: 'bar',
              data: [
                this.pieData.integratedOverdue || 0,
                this.pieData.monthlyOverdue || 0,
              ],
              itemStyle: {
                color: '#f56c6c', // 红色表示超期
              },
              label: {
                show: true,
                position: 'top',
                formatter: '{c}',
                fontSize: 10,
              },
            },
            {
              name: '总数量',
              type: 'bar',
              data: [
                this.pieData.integratedTotal || 0,
                this.pieData.monthlyTotal || 0,
              ],
              itemStyle: {
                color: '#409eff', // 蓝色表示总数
              },
              label: {
                show: true,
                position: 'top',
                formatter: '{c}',
                fontSize: 10,
              },
            },
          ],
        }

        // 添加点击事件监听
        myChart.on('click', (params) => {
          this.handlePieChartClick(params)
        })

        // 设置自适应
        window.addEventListener('resize', () => {
          myChart.resize()
        })

        myChart.setOption(option)
      },

      // 处理催办统计柱状图点击事件
      handlePieChartClick(params) {
        // params.name: '一体化管控措施' 或 '月度评估'
        // params.seriesName: '完成数量', '未完成数量', '超期数量', '总数量'
        const category =
          params.name === '一体化管控措施' ? 'integrated' : 'monthly'

        // 根据系列名称确定type参数
        let type
        switch (params.seriesName) {
          case '总数量':
            type = 1
            break
          case '完成数量':
            type = 2
            break
          case '未完成数量':
            type = 3
            break
          case '超期数量':
            type = 4
            break
          default:
            type = 1
        }

        // 打开弹窗
        this.$refs.reminderDialog.open(category, type, params.seriesName)
      },

      // 清除柱状图
      clearPieChart() {
        const chartDom = this.$refs.pieChart
        if (chartDom) {
          let myChart = echarts.getInstanceByDom(chartDom)
          if (myChart) {
            myChart.clear()
            myChart.dispose()
          }

          myChart = echarts.init(chartDom)

          const emptyOption = {
            title: {
              text: `催办统计`,
              left: 'left',
              textStyle: { fontSize: 14, color: '#FF8C00' },
            },
            xAxis: {
              type: 'category',
              data: ['一体化管控措施', '月度评估'],
            },
            yAxis: {
              type: 'value',
            },
            series: [],
          }
          myChart.setOption(emptyOption)
        }
      },
    },
  }
</script>

<style scoped lang="scss">
  select {
    margin: 20px;
    width: 100px;
  }
</style>
