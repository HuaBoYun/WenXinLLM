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
    name: 'VabChartFunnel',
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
            bottom: 20,
            left: 20,
          },
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b} : {c}%',
          },
          series: {
            name: '漏斗图',
            type: 'funnel',
            left: '20%',
            top: 20,
            bottom: 20,
            width: '60%',
            min: 0,
            max: 100,
            minSize: '0%',
            maxSize: '100%',
            sort: 'descending',
            gap: 2,
            labelLine: {
              length: 10,
              lineStyle: {
                width: 1,
                type: 'solid',
              },
            },
            emphasis: {
              label: {
                fontSize: 12,
              },
            },
            data: [
              { value: 60, name: '访问' },
              { value: 40, name: '咨询' },
              { value: 20, name: '订单' },
              { value: 80, name: '点击' },
              { value: 100, name: '展现' },
            ],
          },
        },
      }
    },
  }
</script>
