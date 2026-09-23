<template>
  <div class="rank">
    <el-card class="rank-card" shadow="hover">
      <template #header>
        <span>
          <vab-icon icon="align-top" />
          消費排行
        </span>
      </template>
      <vab-chart
        class="rank-echart"
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
    components: { VabChart },
    data() {
      return {
        initOptions: {
          renderer: 'svg',
        },
        option: {
          tooltip: {
            trigger: 'axis',
            extraCssText: 'z-index:1',
          },
          grid: {
            top: '0%',
            left: '2%',
            right: '20%',
            bottom: '0%',
            containLabel: true,
          },
          xAxis: [
            {
              splitLine: {
                show: false,
              },
              type: 'value',
              show: false,
            },
          ],
          yAxis: [
            {
              splitLine: {
                show: false,
              },
              axisLine: {
                show: false,
              },
              type: 'category',
              axisTick: {
                show: false,
              },
              data: [
                'good luck',
                '曲丽丽',
                '付小小',
                '林东东',
                '周星星',
                '朱偏右',
              ],
            },
          ],
          series: [
            {
              name: '累计消费',
              type: 'bar',
              barWidth: 15,
              label: {
                show: true,
                position: 'right',
                color: '#1890FF',
                fontSize: 12,
                formatter: ({ data }) => {
                  return `${data}万元`
                },
              },
              itemStyle: {
                borderRadius: 10,
                borderColor: '#fff',
                borderWidth: 2,
              },
              data: [23, 54, 68, 76, 87, 99],
            },
          ],
        },
      }
    },
  }
</script>
