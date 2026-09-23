<template>
  <div>
    <div
      ref="chart"
      style="width: 100%; height: 360px; padding: 10px; margin-bottom: 10px"
    ></div>
    <RiskReviewDialog ref="reviewDialog" />
  </div>
</template>

<script>
  import * as echarts from 'echarts'
  import { getCompanyRiskReview } from '@/api/risk/home.js'
  import RiskReviewDialog from './RiskReviewDialog.vue'

  export default {
    components: {
      RiskReviewDialog,
    },
    props: {
      year: {
        type: [String, Number],
        default: '',
      },
    },
    data() {
      return {
        riskData: [], // 初始化为空数组
      }
    },
    mounted() {
      this.fetchRiskData() // 在组件挂载后获取数据
    },
    watch: {
      riskData: {
        handler() {
          this.initChart()
        },
        deep: true,
      },
      year: {
        immediate: true, // 立即执行一次
        handler(newVal, oldVal) {
          if (newVal != oldVal) {
            this.fetchRiskData()
          }
        },
      },
    },
    methods: {
      async fetchRiskData() {
        const { data, code, msg } = await getCompanyRiskReview({
          year: this.year,
        })

        // 组装数据
        this.riskData = data.x.map((company, index) => {
          return {
            company,
            value: data.y[index], // 风险值
          }
        })
      },
      initChart() {
        const chartDom = this.$refs.chart
        const myChart = echarts.init(chartDom)

        const option = {
          title: {
            text: '重大项目（事项）风险评估情况审查意见',
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
            data: this.riskData.map((item) => item.company),
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
              name: '风险审查数量',
              type: 'bar',
              data: this.riskData.map((item) => item.value),
              label: {
                show: true,
                position: 'top', // 标签显示在柱状图的顶部
                formatter: '{c}', // 显示具体的数值
              },
            },
          ],
        }
        // 添加点击事件监听
        myChart.on('click', (params) => {
          this.$refs.reviewDialog.show({ unitname: params.name })
        })

        // 设置自适应
        window.addEventListener('resize', () => {
          myChart.resize()
        })
        myChart.setOption(option)
      },
    },
  }
</script>

<style scoped></style>
