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
    name: 'VabChartTreemap',
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
            bottom: 60,
            left: 40,
          },
          series: [
            {
              type: 'treemap',
              data: [
                {
                  name: 'nodeA', // First tree
                  value: 10,
                  children: [
                    {
                      name: 'nodeAa', // First leaf of first tree
                      value: 4,
                    },
                    {
                      name: 'nodeAb', // Second leaf of first tree
                      value: 6,
                    },
                  ],
                },
                {
                  name: 'nodeB', // Second tree
                  value: 20,
                  children: [
                    {
                      name: 'nodeBa', // Son of first tree
                      value: 20,
                      children: [
                        {
                          name: 'nodeBa1', // Granson of first tree
                          value: 20,
                        },
                      ],
                    },
                  ],
                },
              ],
            },
          ],
        },
      }
    },
  }
</script>
