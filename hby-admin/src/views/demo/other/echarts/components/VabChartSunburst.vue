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
    name: 'VabChartSunburst',
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
          series: {
            type: 'sunburst',
            data: [
              {
                children: [
                  {
                    value: 5,
                    children: [
                      {
                        value: 1,
                      },
                      {
                        value: 2,
                        children: [
                          {
                            value: 1,
                          },
                        ],
                      },
                      {
                        children: [
                          {
                            value: 1,
                          },
                        ],
                      },
                    ],
                  },
                  {
                    value: 10,
                    children: [
                      {
                        value: 6,
                        children: [
                          {
                            value: 1,
                          },
                          {
                            value: 1,
                          },
                          {
                            value: 1,
                          },
                          {
                            value: 1,
                          },
                        ],
                      },
                      {
                        value: 2,
                        children: [
                          {
                            value: 1,
                          },
                        ],
                      },
                      {
                        children: [
                          {
                            value: 1,
                          },
                        ],
                      },
                    ],
                  },
                ],
              },
              {
                value: 9,
                children: [
                  {
                    value: 4,
                    children: [
                      {
                        value: 2,
                      },
                      {
                        children: [
                          {
                            value: 1,
                          },
                        ],
                      },
                    ],
                  },
                  {
                    children: [
                      {
                        value: 3,
                        children: [
                          {
                            value: 1,
                          },
                          {
                            value: 1,
                          },
                        ],
                      },
                    ],
                  },
                ],
              },
              {
                value: 7,
                children: [
                  {
                    children: [
                      {
                        value: 1,
                      },
                      {
                        value: 3,
                        children: [
                          {
                            value: 1,
                          },
                          {
                            value: 1,
                          },
                        ],
                      },
                      {
                        value: 2,
                        children: [
                          {
                            value: 1,
                          },
                          {
                            value: 1,
                          },
                        ],
                      },
                    ],
                  },
                ],
              },
              {
                children: [
                  {
                    value: 6,
                    children: [
                      {
                        value: 1,
                      },
                      {
                        value: 2,
                        children: [
                          {
                            value: 2,
                          },
                        ],
                      },
                      {
                        value: 1,
                      },
                    ],
                  },
                  {
                    value: 3,
                    children: [
                      {
                        value: 1,
                      },
                      {
                        children: [
                          {
                            value: 1,
                          },
                        ],
                      },
                      {
                        value: 1,
                      },
                    ],
                  },
                ],
              },
            ],
            radius: ['10%', '100%'],
            label: {
              rotate: 'radial',
            },
          },
        },
      }
    },
  }
</script>
