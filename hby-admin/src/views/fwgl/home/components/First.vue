<template>
  <div class="branch">
    <el-card class="branch-card" shadow="hover">
      <!-- <template #header>
        <span>
          <vab-icon icon="donut-chart-fill" />
          专项法务服务费统计
        </span>
      </template> -->
      <vab-chart
        class="branch-echart1"
        :init-options="initOptions"
        :option="option"
        theme="vab-echarts-theme"
      />
    </el-card>
  </div>
</template>

<script>
  import VabChart from '@/extra/VabChart'

  export default {
    components: {
      VabChart,
    },
    data() {
      return {
        initOptions: {
          renderer: 'svg',
        },
        option: {
          title: {
            text: '2019-2021专项法务服务费统计',
            left: 'center',
          },
          tooltip: {
            trigger: 'item',
          },
          legend: {
            orient: 'vertical',
            left: 'right',
          },
          series: [
            {
              name: '专项法务服务费统计',
              type: 'pie',
              radius: '50%',
              data: [
                { value: 50.78, name: '国资公司' },
                { value: 80.4, name: '发展公司' },
                { value: 60.5, name: '富浙租赁' },
                { value: 179.9, name: '富客户C本' },
                { value: 92.28, name: '富客户C产' },
                { value: 68.75, name: '盐业公司' },
                { value: 42.5, name: '富浙科技' },
              ],
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)',
                },
              },
            },
          ],
        },
      }
    },
  }
</script>

<style scoped>
  .branch-echart1 {
    height: 400px !important;
  }
</style>
