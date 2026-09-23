import * as echarts from 'echarts'

export default {
  data() {
    return {
      depreciationCharts: {
        trend: null,
        category: null
      }
    }
  },
  beforeDestroy() {
    // 销毁图表实例
    Object.values(this.depreciationCharts).forEach(chart => {
      if (chart) {
        chart.dispose()
      }
    })
  },
  methods: {
    // 初始化月度折旧趋势图
    initDepreciationTrendChart() {
      const chartDom = document.getElementById('depreciationTrendChart')
      if (!chartDom) return
      
      this.depreciationCharts.trend = echarts.init(chartDom)
      
      const option = {
        title: {
          text: '月度折旧趋势',
          left: 'center',
          textStyle: {
            fontSize: 16,
            fontWeight: 'normal'
          }
        },
        tooltip: {
          trigger: 'axis',
          formatter: '{b}<br/>{a}: ¥{c}万'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            formatter: '¥{value}万'
          }
        },
        series: [
          {
            name: '月度折旧',
            type: 'line',
            smooth: true,
            data: [320, 332, 301, 334, 390, 330, 320, 345, 360, 375, 390, 410],
            itemStyle: {
              color: '#409EFF'
            },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: 'rgba(64, 158, 255, 0.5)' },
                { offset: 1, color: 'rgba(64, 158, 255, 0.1)' }
              ])
            }
          }
        ]
      }
      
      this.depreciationCharts.trend.setOption(option)
    },

    // 初始化资产类别折旧分布图
    initCategoryDistributionChart() {
      const chartDom = document.getElementById('categoryDistributionChart')
      if (!chartDom) return
      
      this.depreciationCharts.category = echarts.init(chartDom)
      
      const option = {
        title: {
          text: '资产类别折旧分布',
          left: 'center',
          textStyle: {
            fontSize: 16,
            fontWeight: 'normal'
          }
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: ¥{c}万 ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          top: 'middle'
        },
        series: [
          {
            name: '折旧金额',
            type: 'pie',
            radius: '60%',
            center: ['60%', '50%'],
            data: [
              { value: 1200, name: '房屋建筑物' },
              { value: 850, name: '机器设备' },
              { value: 620, name: '运输工具' },
              { value: 480, name: '电子设备' },
              { value: 350, name: '办公设备' }
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            },
            label: {
              formatter: '{b}\n¥{c}万\n({d}%)'
            }
          }
        ]
      }
      
      this.depreciationCharts.category.setOption(option)
    }
  }
}

