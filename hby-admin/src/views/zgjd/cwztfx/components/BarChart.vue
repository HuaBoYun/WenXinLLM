<template>
  <div class="bar-chart-wrapper">
    <div class="bar-chart-title">各单位审计项目/任务情况分布图</div>
    <div
      class="bar-chart"
      ref="barChart"
      style="width: 100%; height: 320px"
    ></div>
  </div>
</template>

<script>
  import * as echarts from 'echarts'
  export default {
    props: ['data'],
    mounted() {
      this.initChart()
    },
    methods: {
      initChart() {
        const chart = echarts.init(this.$refs.barChart)
        chart.setOption({
          xAxis: { type: 'value' },
          yAxis: { type: 'category', data: this.data.map((i) => i.name) },
          series: [
            {
              type: 'bar',
              data: this.data.map((i) => i.value),
              itemStyle: { color: '#67C23A' },
            },
          ],
        })
      },
    },
  }
</script>

<style scoped>
  .bar-chart-wrapper {
    margin-top: 16px;
  }
  .bar-chart-title {
    font-size: 20px;
    font-weight: bold;
    color: #444;
    margin-bottom: 8px;
    text-align: left;
  }
</style>
