<template>
  <el-col :lg="8" :md="12" :sm="24" :xl="6" :xs="24">
    <el-card shadow="hover">
      <template #header>
        <span>{{ title }}</span>
      </template>
      <vab-chart
        :init-options="initOptions"
        :option="option"
        theme="vab-echarts-theme"
      />
    </el-card>
  </el-col>
</template>

<script>
  import VabChart from '@/extra/VabChart'

  export default {
    name: 'VabChartRadar',
    components: {
      VabChart,
    },
    props: {
      title: {
        type: String,
        default: '',
      },
    },
    data() {
      return {
        initOptions: {
          renderer: 'svg',
        },
        option: {
          grid: {
            top: 20,
            right: 20,
            bottom: 40,
            left: 40,
          },
          radar: {
            indicator: [
              { name: '销售', max: 6500 },
              { name: '管理', max: 16000 },
              { name: '信息技术', max: 30000 },
              { name: '客服', max: 38000 },
              { name: '研发', max: 52000 },
            ],
          },
          series: [
            {
              name: '预算 vs 开销',
              type: 'radar',
              data: [
                {
                  value: [4200, 3000, 20000, 35000, 50000],
                  name: '预算分配',
                },
                {
                  value: [5000, 14000, 28000, 26000, 42000],
                  name: '实际开销',
                },
              ],
            },
          ],
        },
      }
    },
  }
</script>
