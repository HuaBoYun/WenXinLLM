<template>
  <div class="echarts-container">
    <el-row :gutter="20">
      <vab-chart-line title="折线图" />
      <vab-chart-bar title="柱状图" />
      <vab-chart-pie title="饼状图" />
      <vab-chart-scatter title="散点图" />
      <vab-chart-candlestick title="K线图" />
      <vab-chart-radar title="雷达图" />
      <vab-chart-treemap title="矩形树图" />
      <vab-chart-sunburst title="旭日图" />
      <vab-chart-funnel title="漏斗图" />
      <vab-chart-gauge title="仪表图" />
      <vab-chart-theme-river title="河流流向图" />
    </el-row>
  </div>
</template>

<script>
  import VabChartLine from './components/VabChartLine'
  import VabChartBar from './components/VabChartBar'
  import VabChartPie from './components/VabChartPie'
  import VabChartScatter from './components/VabChartScatter'
  import VabChartCandlestick from './components/VabChartCandlestick'
  import VabChartRadar from './components/VabChartRadar'
  import VabChartTreemap from './components/VabChartTreemap'
  import VabChartSunburst from './components/VabChartSunburst'
  import VabChartFunnel from './components/VabChartFunnel'
  import VabChartGauge from './components/VabChartGauge'
  import VabChartThemeRiver from './components/VabChartThemeRiver'

  export default {
    name: 'Echarts',
    components: {
      VabChartLine,
      VabChartBar,
      VabChartPie,
      VabChartScatter,
      VabChartCandlestick,
      VabChartRadar,
      VabChartTreemap,
      VabChartSunburst,
      VabChartFunnel,
      VabChartGauge,
      VabChartThemeRiver,
    },
    data() {
      return {}
    },
    methods: {},
  }
</script>

<style lang="scss" scoped>
  .echarts-container {
    padding: 0 !important;
    background: $base-color-background !important;
    ::v-deep {
      .echarts {
        width: 100%;
        height: 200px;
      }
    }
  }
</style>
